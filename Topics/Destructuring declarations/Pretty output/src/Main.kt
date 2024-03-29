data class Comment(val id: Int, val body: String, val author: String)

fun printComments(commentsData: MutableList<Comment>){
    commentsData.forEach {(id, body, author) -> println("Author: $author; Text: $body") }
}