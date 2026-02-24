package com.happyending.file.services

import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.util.concurrent.CompletableFuture

@Service
class CloudinaryService(
    private val cloudinary: Cloudinary
) {
    
    private val logger = LoggerFactory.getLogger(CloudinaryService::class.java)
    
    fun uploadFile(
        file: MultipartFile,
        folder: String? = null,
        tags: List<String> = emptyList(),
        isPublic: Boolean = true
    ): CompletableFuture<Map<String, Any>> {
        logger.info("Uploading file: ${file.originalFilename}")
        
        return CompletableFuture.supplyAsync {
            try {
                val uploadOptions = mutableMapOf<String, Any>()
                
                if (folder != null) {
                    uploadOptions["folder"] = folder
                }
                
                if (tags.isNotEmpty()) {
                    uploadOptions["tags"] = tags
                }
                
                uploadOptions["public"] = isPublic
                uploadOptions["resource_type"] = "auto"
                uploadOptions["quality"] = "auto"
                uploadOptions["fetch_format"] = "auto"
                
                val result = cloudinary.uploader().upload(
                    file.bytes,
                    ObjectUtils.asMap(uploadOptions)
                )
                
                logger.info("File uploaded successfully: ${result["public_id"]}")
                result
                
            } catch (e: Exception) {
                logger.error("Failed to upload file: ${e.message}", e)
                throw RuntimeException("Failed to upload file", e)
            }
        }
    }
    
    fun uploadFileFromUrl(
        url: String,
        folder: String? = null,
        tags: List<String> = emptyList(),
        isPublic: Boolean = true
    ): CompletableFuture<Map<String, Any>> {
        logger.info("Uploading file from URL: $url")
        
        return CompletableFuture.supplyAsync {
            try {
                val uploadOptions = mutableMapOf<String, Any>()
                
                if (folder != null) {
                    uploadOptions["folder"] = folder
                }
                
                if (tags.isNotEmpty()) {
                    uploadOptions["tags"] = tags
                }
                
                uploadOptions["public"] = isPublic
                uploadOptions["resource_type"] = "auto"
                uploadOptions["quality"] = "auto"
                uploadOptions["fetch_format"] = "auto"
                
                val result = cloudinary.uploader().upload(
                    url,
                    ObjectUtils.asMap(uploadOptions)
                )
                
                logger.info("File uploaded from URL successfully: ${result["public_id"]}")
                result
                
            } catch (e: Exception) {
                logger.error("Failed to upload file from URL: ${e.message}", e)
                throw RuntimeException("Failed to upload file from URL", e)
            }
        }
    }
    
    fun uploadBase64File(
        base64Data: String,
        folder: String? = null,
        tags: List<String> = emptyList(),
        isPublic: Boolean = true
    ): CompletableFuture<Map<String, Any>> {
        logger.info("Uploading base64 file")
        
        return CompletableFuture.supplyAsync {
            try {
                val uploadOptions = mutableMapOf<String, Any>()
                
                if (folder != null) {
                    uploadOptions["folder"] = folder
                }
                
                if (tags.isNotEmpty()) {
                    uploadOptions["tags"] = tags
                }
                
                uploadOptions["public"] = isPublic
                uploadOptions["resource_type"] = "auto"
                uploadOptions["quality"] = "auto"
                uploadOptions["fetch_format"] = "auto"
                
                val result = cloudinary.uploader().upload(
                    base64Data,
                    ObjectUtils.asMap(uploadOptions)
                )
                
                logger.info("Base64 file uploaded successfully: ${result["public_id"]}")
                result
                
            } catch (e: Exception) {
                logger.error("Failed to upload base64 file: ${e.message}", e)
                throw RuntimeException("Failed to upload base64 file", e)
            }
        }
    }
    
    fun deleteFile(publicId: String): CompletableFuture<Map<String, Any>> {
        logger.info("Deleting file: $publicId")
        
        return CompletableFuture.supplyAsync {
            try {
                val result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap())

                logger.info("File deleted successfully: $publicId")
                result

            } catch (e: Exception) {
                logger.error("Failed to delete file: ${e.message}", e)
                throw RuntimeException("Failed to delete file", e)
            } as Map<String, Any>?
        }
    }
    
    fun deleteFiles(publicIds: List<String>): CompletableFuture<Map<String, Any>> {
        logger.info("Deleting multiple files: $publicIds")
        
        return CompletableFuture.supplyAsync {
            try {
                val result = cloudinary.api().deleteResources(
                    publicIds,
                    ObjectUtils.emptyMap()
                )

                logger.info("Files deleted successfully: $publicIds")
                result

            } catch (e: Exception) {
                logger.error("Failed to delete files: ${e.message}", e)
                throw RuntimeException("Failed to delete files", e)
            }
        }
    }
    
    fun getFileInfo(publicId: String): CompletableFuture<Map<String, Any>> {
        logger.info("Getting file info: $publicId")
        
        return CompletableFuture.supplyAsync {
            try {
                val result = cloudinary.api().resource(
                    publicId,
                    ObjectUtils.emptyMap()
                )
                
                logger.info("File info retrieved successfully: $publicId")
                result
                
            } catch (e: Exception) {
                logger.error("Failed to get file info: ${e.message}", e)
                throw RuntimeException("Failed to get file info", e)
            }
        }
    }
    
    fun generateTransformationUrl(
        publicId: String,
        transformations: Map<String, Any> = emptyMap()
    ): String {
        return try {
            cloudinary.url()
                .transformation(transformations)
                .generate(publicId)
        } catch (e: Exception) {
            logger.error("Failed to generate transformation URL: ${e.message}", e)
            throw RuntimeException("Failed to generate transformation URL", e)
        }
    }
    
    fun generateImageTransformationUrl(
        publicId: String,
        width: Int? = null,
        height: Int? = null,
        crop: String = "fill",
        quality: String = "auto",
        format: String = "auto"
    ): String {
        val transformations = mutableMapOf<String, Any>()
        
        if (width != null) transformations["width"] = width
        if (height != null) transformations["height"] = height
        transformations["crop"] = crop
        transformations["quality"] = quality
        transformations["fetch_format"] = format
        
        return generateTransformationUrl(publicId, transformations)
    }
    
    fun generateVideoTransformationUrl(
        publicId: String,
        width: Int? = null,
        height: Int? = null,
        crop: String = "fill",
        quality: String = "auto",
        format: String = "auto"
    ): String {
        val transformations = mutableMapOf<String, Any>()
        
        if (width != null) transformations["width"] = width
        if (height != null) transformations["height"] = height
        transformations["crop"] = crop
        transformations["quality"] = quality
        transformations["fetch_format"] = format
        transformations["resource_type"] = "video"
        
        return generateTransformationUrl(publicId, transformations)
    }
    
    fun createFolder(folderName: String): CompletableFuture<Map<String, Any>> {
        logger.info("Creating folder: $folderName")
        
        return CompletableFuture.supplyAsync {
            try {
                val result = cloudinary.api().createFolder(
                    folderName,
                    ObjectUtils.emptyMap()
                )
                
                logger.info("Folder created successfully: $folderName")
                result
                
            } catch (e: Exception) {
                logger.error("Failed to create folder: ${e.message}", e)
                throw RuntimeException("Failed to create folder", e)
            }
        }
    }
    
    fun deleteFolder(folderName: String): CompletableFuture<Map<String, Any>> {
        logger.info("Deleting folder: $folderName")
        
        return CompletableFuture.supplyAsync {
            try {
                val result = cloudinary.api().deleteFolder(
                    folderName,
                    ObjectUtils.emptyMap()
                )
                
                logger.info("Folder deleted successfully: $folderName")
                result
                
            } catch (e: Exception) {
                logger.error("Failed to delete folder: ${e.message}", e)
                throw RuntimeException("Failed to delete folder", e)
            }
        }
    }
    
    fun getFolderContents(folderName: String): CompletableFuture<Map<String, Any>> {
        logger.info("Getting folder contents: $folderName")
        
        return CompletableFuture.supplyAsync {
            try {
                val result = cloudinary.api().resourcesByFolder(
                    folderName,
                    ObjectUtils.emptyMap()
                )
                
                logger.info("Folder contents retrieved successfully: $folderName")
                result
                
            } catch (e: Exception) {
                logger.error("Failed to get folder contents: ${e.message}", e)
                throw RuntimeException("Failed to get folder contents", e)
            }
        }
    }
}




