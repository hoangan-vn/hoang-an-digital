package com.happyending.file.services

import com.happyending.file.dtos.CreateFileDTO
import com.happyending.file.dtos.FileUploadResponseDTO
import com.happyending.file.mapper.FileMapper
import com.happyending.file.repositories.FileRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.concurrent.CompletableFuture


@Service
class FileService(
    private val cloudinaryService: CloudinaryService,
    private val fileRepository: FileRepository,
    private val fileMapper: FileMapper
) {

    fun uploadFile(file: MultipartFile, createFileDTO: CreateFileDTO): CompletableFuture<FileUploadResponseDTO> {
        return cloudinaryService.uploadFile(
            file = file,
            folder = createFileDTO.folder,
            tags = createFileDTO.tags,
            isPublic = createFileDTO.isPublic
        ).thenApply { cloudinaryResult ->
            val fileEntity = fileMapper.toEntity(createFileDTO, cloudinaryResult)
            val savedFile = fileRepository.save(fileEntity)
            val fileDTO = fileMapper.toDTO(savedFile)

            // Assuming you have a way to generate these URLs
            val uploadUrl = "/api/files/upload" // Placeholder
            val deleteUrl = "/api/files/${savedFile.id}" // Placeholder

            fileMapper.toUploadResponse(fileDTO, uploadUrl, deleteUrl)
        }
    }
}
