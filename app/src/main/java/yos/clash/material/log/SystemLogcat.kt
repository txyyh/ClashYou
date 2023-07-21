package yos.clash.material.log

object SystemLogcat {
    private val command = arrayOf(
        "logcat",
        "-d",
        "-s",
        "Go",
        "DEBUG",
        "AndroidRuntime",
        "ClashForAndroid",
        "LwIP",
    )

    fun dumpCrash(): String {
        return try {
            val process = Runtime.getRuntime().exec(command)

            val result = process.inputStream.use { stream ->
                stream.reader().readLines()
                    .filterNot { it.startsWith("------") }
                    .joinToString("\n")
            }

            process.waitFor()

            result.trim()
        } catch (e: Exception) {
            ""
        }
    }
}
