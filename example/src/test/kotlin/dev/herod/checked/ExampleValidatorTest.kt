package dev.herod.checked

import org.junit.Assert.assertNotNull
import org.junit.Test

class ExampleValidatorTest {

    @Test
    fun `null property model is invalid`() {

        assertNotNull(object : ExampleModel {
            override val stringNonEmpty: String? = "NON EMPTY"
            override val stringNonBlank: String? = "sssss"
            override val stringOrEmptyIfNull: String? = null
            override val stringAtLeastLengthFour: String? = "valid stringAtLeastLengthFour"
            override val lol: String? = "valid lol"
            override val stringNonNull: String? = "valid stringNonNull"
            override val numberBetweenZeroAndTwo: Int? = 0
            override val child: ExampleModel? = this
        }.checked())
    }

    @Test(expected = IllegalArgumentException::class)
    fun `string non empty is empty`() {

        assertNotNull(object : ExampleModel {
            override val stringNonEmpty: String? = ""
            override val stringNonBlank: String? = ""
            override val stringOrEmptyIfNull: String? = null
            override val stringAtLeastLengthFour: String? = "valid stringAtLeastLengthFour"
            override val lol: String? = "valid lol"
            override val stringNonNull: String? = "valid stringNonNull"
            override val numberBetweenZeroAndTwo: Int? = 0
            override val child: ExampleModel? = this
        }.checked())
    }

    @Test
    fun `nonnull property model but low number is invalid`() {

        assertNotNull(object : ExampleModel {
            override val stringNonEmpty: String? = " "
            override val stringNonBlank: String? = "aaa"
            override val stringOrEmptyIfNull: String? = ""
            override val stringAtLeastLengthFour: String? = ""
            override val lol: String? = "non empty"
            override val stringNonNull: String? = ""
            override val numberBetweenZeroAndTwo: Int? = 1
            override val child: ExampleModel? = this
        }.checked())
    }

    @Test
    fun `nonnull property model is valid`() {

        assertNotNull(object : ExampleModel {
            override val stringNonEmpty: String? = "non empty"
            override val stringNonBlank: String? = "hh"
            override val stringNonNull: String? = ""
            override val numberBetweenZeroAndTwo: Int? = 1
            override val child: ExampleModel? = this
            override val stringOrEmptyIfNull: String? = ""
            override val stringAtLeastLengthFour: String? = ""
            override val lol: String? = "lol cannot be empty"
        }.checked())
    }

    @Test(expected = IllegalArgumentException::class)
    fun `lol cannot be empty`() {
        object : ExampleModel {
            override val stringNonEmpty: String? = ""
            override val stringNonBlank: String? = ""
            override val stringNonNull: String? = ""
            override val numberBetweenZeroAndTwo: Int? = 1
            override val child: ExampleModel? = this
            override val stringOrEmptyIfNull: String? = ""
            override val stringAtLeastLengthFour: String? = ""
            override val lol: String? = ""
        }.checked()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `lol cannot be null`() {
        object : ExampleModel {
            override val stringNonEmpty: String? = ""
            override val stringNonBlank: String? = ""
            override val stringNonNull: String? = ""
            override val numberBetweenZeroAndTwo: Int? = 1
            override val child: ExampleModel? = this
            override val stringOrEmptyIfNull: String? = ""
            override val stringAtLeastLengthFour: String? = ""
            override val lol: String? = null
        }.checked()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `poo cannot be null`() {
        object : ExampleModel {
            override val stringNonEmpty: String? = "lol"
            override val stringNonBlank: String? = "   "
            override val stringNonNull: String? = ""
            override val numberBetweenZeroAndTwo: Int? = 1
            override val child: ExampleModel? = this
            override val stringOrEmptyIfNull: String? = ""
            override val stringAtLeastLengthFour: String? = null
            override val lol: String? = null
        }.checked()
    }
}
