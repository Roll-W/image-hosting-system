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

package space.lingu.imagehosting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import space.lingu.common.command.CommandRunner;
import space.lingu.i18n.I18nLocaleFile;
import space.lingu.imagehosting.service.image.ImageService;
import space.lingu.imagehosting.service.user.UserService;

@SpringBootApplication
@I18nLocaleFile(className = "IID")
public class ImageHostingSystemApplication implements ApplicationRunner {
    private final Logger logger = LoggerFactory.getLogger(ImageHostingSystemApplication.class);

    private static final CommandRunner runner;
    static {
        runner = CommandRunner.systemCommandReader();
        runner.showUnknownCommandInfo();
        runner.disableDefaultExitCommand();
        runner.showConsolePrefix("lingu> ");
    }


    public static void main(String[] args) {
        SpringApplication.run(ImageHostingSystemApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        UserCommand userCommand = new UserCommand(runner.getReader(), runner.getWriter());
//        runner.registerCommand(userCommand);
//        runner.start();
    }

    UserService userService;
    ImageService imageService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setImageService(ImageService imageService) {
        this.imageService = imageService;
    }
}
