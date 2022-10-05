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

import {axios_config, axquest as axios} from "./axios_config";

export async function getCurrentUser(url) {
    let data;
    await axios.get(url + "/api/user/current")
        .then(r => {
            console.log("get result: " + r)
            if (r.status === 200) {
                data = r.data
            }
        })
        .catch(error => {
            data = error.response.data
        })

    return data
}

export async function loginUser(url, username, password, rememberMe) {
    let data;

    await axios.post(url + "/api/user/login", {
        username: username,
        password: password,
    }, axios_config)
        .then(r => {
            data = r.data
        })
        .catch(error => {
            data = error.response.data
        })

    return data
}