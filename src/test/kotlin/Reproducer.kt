import io.quarkus.test.junit.QuarkusTest
import jakarta.enterprise.context.ApplicationScoped
import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

data class TestDto(val a: String, val b: Int)

@ApplicationScoped
class TourPlanLocationReferenceValidator :
    ConstraintValidator<ValidLocationReferences, TestDto> {
    override fun isValid(value: TestDto?, context: ConstraintValidatorContext?): Boolean {
        return value?.a == "foobar"
    }
}

@Target(AnnotationTarget.CLASS)
@Constraint(validatedBy = [TourPlanLocationReferenceValidator::class])
@MustBeDocumented
annotation class ValidLocationReferences(
    val message: String =
        "{com.cargonexx.vehiclerouting.rest.dto.validation.ValidLocationReferences.message}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
)


@QuarkusTest
class Reproducer {
    companion object {
        @Suppress("LongMethod")
        @JvmStatic
        fun testData(): List<Arguments> =
            listOf(
                Arguments.of(
                    "/tour-plan-validation/missing-stop-location-id.json",
                    String::class,
                    "",
                ),
                Arguments.of(
                    "foobar",
                    String::class,
                    "",
                ),
                Arguments.of(
                    "/tour-plan-validation/missing-stop-location-id.json",
                    ValidLocationReferences::class,
                    "",
                ),
                Arguments.of(
                    "foobar",
                    ValidLocationReferences::class,
                    "",
                ),
            )
    }

    @ParameterizedTest
    @MethodSource("testData")
    fun test(resourcePath: String, expectedClass: KClass<*>, expectedPropertyName: String) {

    }

}
