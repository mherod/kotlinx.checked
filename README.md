# kotlinx.checked

```
implementation project(':codegen-annotation')
kapt project(':codegen-processor')
```

```
@Checked
interface ExampleModel {
    @CheckedOrEmpty
    val stringOrEmptyIfNull: String?

    @CheckedNonNull
    val stringNonNull: String?

    @CheckedNonEmpty
    val stringNonEmpty: String?

    @CheckedNonBlank
    val stringNonBlank: String?

    @CheckedLength(atLeast = 4)
    val stringAtLeastLengthFour: String?

    @CheckedNonBlank
    val lol: String?

    @CheckedRange(start = 0, endInclusive = 2)
    val numberBetweenZeroAndTwo: Int?

    @CheckedNonNull
    val child: ExampleModel?
}
```
... produces ...

```
data class CheckedExampleModel(
    override val stringOrEmptyIfNull: String,
    override val stringNonNull: String,
    override val stringNonEmpty: String,
    override val stringNonBlank: String,
    override val stringAtLeastLengthFour: String,
    override val lol: String,
    override val numberBetweenZeroAndTwo: Int,
    override val child: dev.herod.checked.ExampleModel
) : ExampleModel

fun ExampleModel?.checked(): CheckedExampleModel? {
    if (this == null) throw IllegalArgumentException("dev.herod.checked.ExampleModel was null")
    val _stringOrEmptyIfNull = stringOrEmptyIfNull.orEmpty()
    // String
    val _stringNonNull = stringNonNull?: throw IllegalArgumentException("stringNonNull was null")
    // String
    val _stringNonEmpty = stringNonEmpty?: throw IllegalArgumentException("stringNonEmpty was null")
    if (_stringNonEmpty.isEmpty()) throw IllegalArgumentException("stringNonEmpty was empty")
    // String
    val _stringNonBlank = stringNonBlank?: throw IllegalArgumentException("stringNonBlank was null")
    if (_stringNonBlank.isBlank()) throw IllegalArgumentException("stringNonBlank was blank")
    // String
    val _stringAtLeastLengthFour = stringAtLeastLengthFour?: throw
            IllegalArgumentException("stringAtLeastLengthFour was null")
    // String
    val _lol = lol?: throw IllegalArgumentException("lol was null")
    if (_lol.isBlank()) throw IllegalArgumentException("lol was blank")
    // String
    val _numberBetweenZeroAndTwo = numberBetweenZeroAndTwo?: throw
            IllegalArgumentException("numberBetweenZeroAndTwo was null")
    // Int
    val child_isThis = this == child
    val _child = child?.let { if (child_isThis) it else it.checked() } ?: throw
            IllegalArgumentException("child was null")
    // dev.herod.checked.ExampleModel
    return try {
        CheckedExampleModel(stringOrEmptyIfNull = _stringOrEmptyIfNull, stringNonNull =
                _stringNonNull, stringNonEmpty = _stringNonEmpty, stringNonBlank = _stringNonBlank,
                stringAtLeastLengthFour = _stringAtLeastLengthFour, lol = _lol,
                numberBetweenZeroAndTwo = _numberBetweenZeroAndTwo, child = _child)
    }
    catch(throwable: Throwable) {
        null
    }
}
```