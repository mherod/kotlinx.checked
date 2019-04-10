package dev.herod.checked

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
