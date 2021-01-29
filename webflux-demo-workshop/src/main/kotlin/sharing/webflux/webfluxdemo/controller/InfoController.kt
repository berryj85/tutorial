package sharing.webflux.webfluxdemo.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/info")
class InfoController {
    @GetMapping
    fun getInfo():String {
        return "Hello"
    }
}