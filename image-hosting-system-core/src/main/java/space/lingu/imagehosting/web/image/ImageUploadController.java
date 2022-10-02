/*
 * Copyright (C) 2022 Lingu.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package space.lingu.imagehosting.web.image;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import space.lingu.imagehosting.data.dto.HttpResponseEntity;
import space.lingu.imagehosting.service.image.ImageService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author RollW
 */
@RestController
@ImageApi
public class ImageUploadController {
    final ImageService imageService;

    public ImageUploadController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public HttpResponseEntity<String> uploadImage(HttpServletRequest request,
                                                  @RequestPart(name = "image") MultipartFile file)
            throws IOException {

        logger.info("upload file, name:{}, size: {}",
                file.getName(), file.getSize());
        return HttpResponseEntity.create(
                imageService.saveImage(request, file.getBytes()).toResponseBody()
        );
    }


    private final Logger logger = LoggerFactory.getLogger(ImageUploadController.class);

}
