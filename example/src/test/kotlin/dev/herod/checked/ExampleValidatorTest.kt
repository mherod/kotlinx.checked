package dev.herod.checked

import org.junit.Assert.assertNotNull
import org.junit.Test

class ExampleValidatorTest {

    @Test
    fun `null property model is invalid`() {

        assertNotNull(object : ExampleModel {
            override val garbage: String? = null
            override val poo: String? = "valid poo"
            override val lol: String? = "valid lol"
            override val another: String? = "valid another"
            override val number: Int? = 0
            override val child: ExampleModel? = this
        }.checked())
    }

    @Test
    fun `nonnull property model but low number is invalid`() {

        assertNotNull(object : ExampleModel {
            override val garbage: String? = ""
            override val poo: String? = ""
            override val lol: String? = "non empty"
            override val another: String? = ""
            override val number: Int? = 1
            override val child: ExampleModel? = this
        }.checked())
    }

    @Test
    fun `nonnull property model is valid`() {

        assertNotNull(object : ExampleModel {
            override val another: String? = ""
            override val number: Int? = 1
            override val child: ExampleModel? = this
            override val garbage: String? = ""
            override val poo: String? = ""
            override val lol: String? = "lol cannot be empty"
        }.checked())
    }

    @Test(expected = IllegalArgumentException::class)
    fun `lol cannot be empty`() {
        object : ExampleModel {
            override val another: String? = ""
            override val number: Int? = 1
            override val child: ExampleModel? = this
            override val garbage: String? = ""
            override val poo: String? = ""
            override val lol: String? = ""
        }.checked()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `lol cannot be null`() {
        object : ExampleModel {
            override val another: String? = ""
            override val number: Int? = 1
            override val child: ExampleModel? = this
            override val garbage: String? = ""
            override val poo: String? = ""
            override val lol: String? = null
        }.checked()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `poo cannot be null`() {
        object : ExampleModel {
            override val another: String? = ""
            override val number: Int? = 1
            override val child: ExampleModel? = this
            override val garbage: String? = ""
            override val poo: String? = null
            override val lol: String? = null
        }.checked()
    }
}
