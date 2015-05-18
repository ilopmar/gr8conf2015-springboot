@RestController
class ThisWillActuallyRun {

    @RequestMapping("/")
    String home() {
        'Hello GR8Conf!'
    }
}
