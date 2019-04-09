# kotlinx.checked

```
implementation project(':codegen-annotation')
kapt project(':codegen-processor')
```

```
@Checked
interface ExampleModel {
    @CheckedOrEmpty
    val garbage: String?

    @CheckedLength(atLeast = 4)
    val poo: String?

    @CheckedNonEmpty
    val lol: String?

    @CheckedNonNull
    val another: String?

    @CheckedRange(start = 0, endInclusive = 2)
    val number: Int?

    @CheckedNonNull
    val child: ExampleModel?
}
```
... produces ...

```
data class CheckedExampleModel(
    override val garbage: String,
    override val poo: String,
    override val lol: String,
    override val another: String,
    override val number: Int,
    override val child: ExampleModel
) : ExampleModel

fun ExampleModel?.checked(): CheckedExampleModel? {
    if (this == null) throw IllegalArgumentException("ExampleModel was null")
    val _garbage = garbage.orEmpty()
    val _poo = poo?: throw IllegalArgumentException("poo was null")
    val _lol = lol?: throw IllegalArgumentException("lol was null")
    if (_lol.isEmpty()) throw IllegalArgumentException("lol was empty")
    val _another = another?: throw IllegalArgumentException("another was null")
    val _number = number?: throw IllegalArgumentException("number was null")
    val _child = child?: throw IllegalArgumentException("child was null")
    return try {
        CheckedExampleModel(garbage = _garbage, poo = _poo, lol = _lol, another = _another, number =
                _number, child = _child)
    }
    catch(throwable: Throwable) {
        null
    }
}
```