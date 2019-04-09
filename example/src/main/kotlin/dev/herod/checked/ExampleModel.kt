package dev.herod.checked

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
