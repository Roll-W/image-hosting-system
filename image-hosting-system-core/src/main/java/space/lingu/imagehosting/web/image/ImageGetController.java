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

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import space.lingu.imagehosting.service.image.ImageService;
import space.lingu.imagehosting.web.PublicUrlApi;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author RollW
 */
@RestController
@PublicUrlApi
public class ImageGetController {
    final ImageService imageService;

    public ImageGetController(ImageService imageService) {
        this.imageService = imageService;
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/image/{userId}/{id}", produces = "image/*")
    public byte[] getImage(@PathVariable("userId") Long userId,
                           @PathVariable("id") String id) throws IOException {
        byte[] bytes = imageService.getImageBytes(userId, id);
        if (bytes == null) {
            throw new FileNotFoundException("not found resource id: " + id);
        }
        return bytes;
    }
}
