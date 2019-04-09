package dev.herod.checked

@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.SOURCE)
annotation class Checked

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class CheckedLength(val atLeast: Int)

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FILE)
@Retention(AnnotationRetention.SOURCE)
annotation class CheckedNonNull

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class CheckedNonEmpty

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class CheckedNonBlank

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class CheckedOrEmpty

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class CheckedRange(val start: Int, val endInclusive: Int)