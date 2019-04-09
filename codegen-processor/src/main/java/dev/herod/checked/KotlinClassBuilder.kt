package dev.herod.checked

import com.squareup.kotlinpoet.*
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.type.TypeMirror

private fun fixKName(input: String): String {
    return input.replace("get(\\w)".toRegex(), "$1")
            .let { "${it.substring(0, 1).toLowerCase()}${it.substring(1)}" }
}

class KotlinClassBuilder(
        val className: TypeMirror,
        val elements: List<Element>,
        val allNonNullElements: Set<Element>,
        val roundEnvironment: RoundEnvironment,
        val throws: Boolean
) {

    private val classNameFull = className.toString()

    private val targetPackage = classNameFull.substringBeforeLast('.')

    private val simpleName = classNameFull.substringAfterLast('.')

    fun getContent(): FileSpec {

        val checkedName = "Checked$simpleName"

        val checkedTypeSpec = TypeSpec.classBuilder(checkedName)
                .addModifiers(KModifier.DATA)
                .addSuperinterface(className.asTypeName())
                .primaryConstructor(elements.fold(FunSpec.constructorBuilder()) { a, b ->
                    a.addParameter(
                            fixKName(b.simpleName.toString()),
                            b.getTypeVariableName()
                    )
                }.build())
                .let { builder ->
                    elements.fold(builder) { a, b ->
                        val className = fixKName(b.simpleName.toString())
                        a.addProperty(PropertySpec.builder(
                                className,
                                b.getTypeVariableName(),
                                KModifier.PUBLIC,
                                KModifier.OVERRIDE
                        ).initializer(className).build())
                    }
                }
                .build()

        val returnType = TypeVariableName("T").copy(reified = true)

        val typeVariableName = TypeVariableName(checkedName)
        val useFunction = FunSpec.builder("use")
                .addModifiers(KModifier.INLINE)
                .addTypeVariable(returnType)
                .receiver(className.asTypeName().copy(nullable = true))
                .returns(returnType.copy(nullable = true))
                .addParameter(
                        "function",
                        LambdaTypeName.get(
                                parameters = *arrayOf(typeVariableName),
                                returnType = returnType
                        )
                )
                .addComment("TEST")
                .addStatement("return checked()?.let { function(it) }")
                .build()

        val checkedFunction = FunSpec.builder("checked")
                .receiver(className.asTypeName().copy(nullable = true))
                .returns(typeVariableName.copy(nullable = true))
                .addStatement("if (this == null) ${returnOrThrow("$className was null")}")
                .let { b ->
                    with(roundEnvironment) {
                        elements.fold(b) { builder, element ->
                            val kName = element.kName()
                            try {
                                val nonNull = element.isAnnotated(allNonNullElements)
                                val orEmpty = element.isAnnotated(checkedOrEmptyElements)
                                val nonEmpty = element.isAnnotated(checkedNonEmptyElements)
                                val nonBlank = element.isAnnotated(checkedNonBlankElements)
                                val statement1 = listOf("val _$kName = $kName${when {
                                    orEmpty -> ".orEmpty()"
                                    nonNull -> "?: ${returnOrThrow("$kName was null")}"
                                    else -> ""
                                }.trim()}", when {
                                    nonEmpty -> "if (_$kName.isEmpty()) ${returnOrThrow("$kName was empty")}"
                                    nonBlank -> "if (_$kName.isBlank()) ${returnOrThrow("$kName was blank")}"
                                    else -> ""
                                }.trim()).filterNot { it.isBlank() }
                                statement1.fold(builder) { builder1, s ->
                                    builder1.addStatement(s)
                                }
                            } catch (throwable: Throwable) {
                                builder
                            }
                        }
                    }
                }
                .beginControlFlow("return try {")
                .addStatement("${
                elements.fold("${typeVariableName.name}(") { acc, element ->
                    val kName = element.kName()
                    "$acc$kName = _$kName, "
                }.trim { it == ',' || it.isWhitespace() }
                })")
                .endControlFlow()
                .beginControlFlow("catch(throwable: Throwable)")
                .addStatement("null")
                .endControlFlow()
                .build()

        return FileSpec.builder(targetPackage, checkedName)
                .addComment("${allNonNullElements.map { it.toString() }}")
                .addType(checkedTypeSpec)
                .addFunction(useFunction)
                .addFunction(checkedFunction)
                .build()
    }

    private fun returnOrThrow(message: String): String = when {
        throws && !message.isBlank() -> "throw IllegalArgumentException(\"$message\")"
        else -> "return null"
    }

    private fun Element.isAnnotated(elements: Set<Element>): Boolean {
        val kName = kName()
        return kName in elements
                .map { it.simpleName.toString().substringBefore('$') }
    }

    private fun Element.getTypeVariableName(): TypeVariableName {
        val name = asType()
                .toString()
                .trim { c -> !c.isLetter() }
                .replace("java.lang.", "")
                .replace("Integer", "Int")
        return TypeVariableName(name)
                .copy(nullable = isAnnotated(allNonNullElements).not())
    }
}

fun Element.kName() = fixKName(simpleName.toString())
