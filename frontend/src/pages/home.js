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

import React from "react";
import {Helmet} from "react-helmet";
import styles from "../index.module.css"
import logo from "../logo.svg"
import {Link} from "react-router-dom";

class HomeHero extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className={`${styles.App} ${styles.light}`} >
            <div className={"px-4 py-5 my-5 text-center"}>
                <img className={`d-block ${styles["text-opacity-w-75"]} mx-auto mb-4`} src={logo} alt="" width="72"
                     height="72" />
                <h1 className={`display-5 fw-bold ${styles["text-opacity-w-75"]}`}>{this.props.systemName || "Lingu Image Hosting System"}</h1>
                <div className={"col-lg-6 mx-auto"}>
                    <p className={"lead mb-4"}>
                        {this.props.intro}
                    </p>
                    <div className={"d-grid gap-2 d-sm-flex justify-content-sm-center"}>
                        <Link to={'/upload'}>
                            <button type="button" className={`${styles["text-opacity-w-75"]} btn btn-primary btn-lg px-4 gap-3`}>
                                {this.props.uploadButton || "Upload an Image"}</button>
                        </Link>
                    </div>
                </div>
            </div>
        </div>);
    }
}

export function Home() {
    return (
        <>
            <Helmet>
                <title>Home | Lingu Image Hosting</title>
            </Helmet>
            <HomeHero systemName={"Lingu Image Hosting System"} intro={"Lingu Image Hosting System provides you with an easy way to" +
                " host your image files, allowing you to share your images with everyone."}
                      uploadButton={"Upload an Image"} />
        </>

    );
}


export default Home;