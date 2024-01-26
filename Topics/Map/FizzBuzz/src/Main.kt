fun iterator(map: Map<String, Int>) {

    map.forEach { key, value ->
        when {
            value % 5 == 0 -> println("Buzz")
            value % 3 == 0 -> println("Fizz")
            else -> println("FizzBuzz")
        }
    }

}