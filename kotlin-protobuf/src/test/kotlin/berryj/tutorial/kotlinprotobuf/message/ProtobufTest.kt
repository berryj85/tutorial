package berryj.tutorial.kotlinprotobuf.message

import com.google.protobuf.util.JsonFormat
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.platform.commons.logging.LoggerFactory
import java.io.BufferedWriter
import java.io.FileOutputStream
import java.io.FileWriter

@ExtendWith(MockKExtension::class)
class ProtobufTest {
    val logger = LoggerFactory.getLogger(ProtobufTest::class.java)

    @Test
    fun should_success_when_create_customer_message() {
        // Given
        val customerId = 1
        val firstName = "FirstName"
        val lastName = "LastName"
        val activeStatus = true
        val phoneNumbers = arrayOf("089xxxxxx1", "089xxxxxx2")
        val customer = Customer.newBuilder().apply {
            this.customerId = customerId
            this.firstname = firstName
            this.lastname = lastName
            this.activeStatus = activeStatus
            this.addPhoneNumbers(phoneNumbers[0])
            this.addPhoneNumbers(phoneNumbers[1])
        }.build()
        Assertions.assertTrue {
            customer.customerId == customerId &&
                    customer.firstname == firstName &&
                    customer.lastname == lastName &&
                    customer.activeStatus == activeStatus &&
                    customer.phoneNumbersCount == 2
        }
    }

    @Test
    fun should_nullPointerException_when_setNulltoCustomerMessage() {
        val customerId = 1
        val firstName = null
        val lastName = "LastName"
        val activeStatus = true
        val phoneNumbers = arrayOf("089xxxxxx1", "089xxxxxx2")
        Assertions.assertThrows(NullPointerException::class.java) {
            val customer = Customer.newBuilder().apply {
                this.customerId = customerId
                this.firstname = firstName
                this.lastname = lastName
                this.activeStatus = activeStatus
                this.addPhoneNumbers(phoneNumbers[0])
                this.addPhoneNumbers(phoneNumbers[1])
            }.build()
        }
    }

    @Test
    fun generateCustomerMessageTofile() {

        val customerId = 1
        val firstName = "FirstName"
        val lastName = "LastName"
        val activeStatus = true
        val phoneNumbers = arrayOf("089xxxxxx1", "089xxxxxx2")
        val customer = Customer.newBuilder().apply {
            this.customerId = customerId
            this.firstname = firstName
            this.lastname = lastName
            this.activeStatus = activeStatus
            this.addPhoneNumbers(phoneNumbers[0])
            this.addPhoneNumbers(phoneNumbers[1])
        }.build()
        val typeRegister = JsonFormat.TypeRegistry.newBuilder().add(Customer.getDescriptor()).build()
        val jsonPrinter = JsonFormat.printer().usingTypeRegistry(typeRegister)
        val serializedCustomer = jsonPrinter.print(customer)
        BufferedWriter(FileWriter("./customer.json")).run {
            this.write(serializedCustomer)
            this.flush()
            this.close()
        }
        customer.writeTo(FileOutputStream("./customer.pb"))

    }

}