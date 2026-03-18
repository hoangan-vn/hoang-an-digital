package com.main.server.presentation.rest

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/media")
class StorageController(
    private val cloudinaryService: CloudinaryMediaService,
) {
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun upload(
        @RequestPart("file") file: MultipartFile,
        @RequestParam(required = false) folder: String?,
        @RequestParam(required = false) publicId: String?,
        @RequestParam(required = false, defaultValue = "false") overwrite: Boolean,
    ): ResponseEntity<CloudinaryMediaAsset> =
        ResponseEntity.status(HttpStatus.CREATED).body(
            cloudinaryService.upload(
                file = file,
                folder = folder,
                publicId = publicId,
                overwrite = overwrite,
            ),
        )

    @GetMapping
    fun get(
        @RequestParam publicId: String,
    ): CloudinaryMediaAsset = cloudinaryService.get(publicId)

    @DeleteMapping
    fun delete(
        @RequestParam publicId: String,
    ): CloudinaryDeleteResult = cloudinaryService.delete(publicId)
}
