package berryj.tutorial.springboot.protobuf.microservice.config


import berryj.tutorial.springboot.protobuf.microservice.message.Customer
import com.google.protobuf.util.JsonFormat
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.MediaType
import org.springframework.http.converter.protobuf.ProtobufJsonFormatHttpMessageConverter

@Configuration
class ProtobufConfiguration {


    @Bean
    fun jsonFormatTypeRegistry() = JsonFormat.TypeRegistry.newBuilder()
            .add(Customer.getDescriptor())
            .build()
    @Bean
    fun jsonFormatPrinter(typeRegistry: JsonFormat.TypeRegistry): JsonFormat.Printer = JsonFormat.printer()
            .usingTypeRegistry(typeRegistry)
    @Bean
    fun jsonFormatParser(typeRegistry: JsonFormat.TypeRegistry): JsonFormat.Parser = JsonFormat.parser()
            .usingTypeRegistry(typeRegistry)
            .ignoringUnknownFields()
    @Bean
    @Primary
    fun protobufHttpMessageConverter(parser: JsonFormat.Parser, printer: JsonFormat.Printer): ProtobufJsonFormatHttpMessageConverter {
        return ProtobufJsonFormatHttpMessageConverter(parser,printer).apply {
            this.supportedMediaTypes = arrayListOf(MediaType.APPLICATION_JSON, ProtobufJsonFormatHttpMessageConverter.PROTOBUF)
        }
    }

}