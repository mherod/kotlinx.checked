package dev.herod.checked

import com.google.auto.service.AutoService
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(FileGenerator.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class FileGenerator : AbstractProcessor() {

    override fun getSupportedAnnotationTypes(): MutableSet<String> = mutableSetOf(
            Checked::class.java.name,
            CheckedNonNull::class.java.name,
        CheckedNonEmpty::class.java.name,
        CheckedNonBlank::class.java.name,
            CheckedOrEmpty::class.java.name
    )

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latest()

    override fun process(set: MutableSet<out TypeElement>, roundEnvironment: RoundEnvironment): Boolean {

        with(roundEnvironment) {
            getElementsAnnotatedWith(Checked::class.java).forEach { element ->

                val packageName = processingEnv.elementUtils.getPackageOf(element).toString()

                val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
                        ?: "app/src/main/java/"

                val dir = kaptKotlinGeneratedDir.replace("kaptKotlin", "kapt") +
                        "/" +
                        packageName.replace('.', '/')

                val fileSpec = KotlinClassBuilder(
                        throws = true,
                        roundEnvironment = roundEnvironment,
                        allNonNullElements = listOfNotNull(
                                checkedNonNullElements,
                                checkedNonEmptyElements,
                                checkedNonBlankElements,
                                checkedLengthElements,
                                checkedRangeElements,
                                checkedOrEmptyElements
                        ).flatten().toSet(),
                        className = element.asType(),
                        elements = element.enclosedElements.filter { it.kName() != "defaultImpls" }
                ).getContent()

                val fileName = fileSpec.name
                File(dir).mkdirs()
                File(dir, "$fileName.kt").writeText(fileSpec.toString())
            }
        }
        return true
    }

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }
}

val RoundEnvironment.checkedNonNullElements: MutableSet<out Element>
    get() = getElementsAnnotatedWith(CheckedNonNull::class.java)

val RoundEnvironment.checkedNonEmptyElements: MutableSet<out Element>
    get() = getElementsAnnotatedWith(CheckedNonEmpty::class.java)

val RoundEnvironment.checkedNonBlankElements: MutableSet<out Element>
    get() = getElementsAnnotatedWith(CheckedNonBlank::class.java)

val RoundEnvironment.checkedLengthElements: MutableSet<out Element>
    get() = getElementsAnnotatedWith(CheckedLength::class.java)

val RoundEnvironment.checkedRangeElements: MutableSet<out Element>
    get() = getElementsAnnotatedWith(CheckedRange::class.java)

val RoundEnvironment.checkedOrEmptyElements: MutableSet<out Element>
    get() = getElementsAnnotatedWith(CheckedOrEmpty::class.java)

