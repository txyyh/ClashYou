package yos.clash.material.service.document

interface Document {
    val id: String
    val name: String
    val mimeType: String
    val size: Long
    val updatedAt: Long
    val flags: Set<Flag>
}