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
import Helmet from "react-helmet";

import styles from "../index.module.css";
import logo from "../logo.svg"

import {withRouter} from 'react-router-dom'
import {registerUser} from "../action/userAction";
import {createToast} from "../action/toast";


class RegisterForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            username: "",
            password: "",
            email: "",
        }

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event) {
        const target = event.target
        const value = target.type === 'checkbox' ? target.checked : target.value;
        const name = target.name;

        this.setState({
                [name]: value
            }
        );
    }

    changePage = (email) => {
        let {history} = this.props
        history.push({
            pathname: `/register/info?${email}`
        })
    }

    handleSubmit(event) {
        registerUser(
            this.props.url, this.state.username,
            this.state.password, this.state.email)
            .then(res => {
                if (res === null) {
                    console.log("null res")
                    return
                }

                if (res.errorCode === "00000") {
                    this.changePage(res.data.email)
                } else {
                    createToast("", res.message)
                }
                console.log(res)
            })
        event.preventDefault();
    }

    render() {
        return (
            <>
                <div className="w-25 m-auto text-center">
                    <form onSubmit={this.handleSubmit}>
                        <img className={`${styles["text-opacity-w-50"]} mb-4`} src={logo} alt="" width="72"
                             height="72"/>
                        <h1 className={`${styles["light"]} h3 mb-3 fw-normal`}>Register</h1>
                        <div className={"form-floating"}>
                            <input type="text" className={`${styles["text-opacity-w-50"]} form-control`}
                                   id="username-input"
                                   value={this.state.username}
                                   placeholder="Username" name={"username"} onChange={this.handleChange}/>
                            <label htmlFor="username-input">Username</label>
                        </div>
                        <div className="form-floating">
                            <input type="password" className={`${styles["text-opacity-w-50"]} form-control`}
                                   id="password-input"
                                   name={"password"}
                                   value={this.state.password}
                                   placeholder="Password" onChange={this.handleChange}/>
                            <label htmlFor="password-input">Password</label>
                        </div>
                        <div className="form-floating">
                            <input type="email" className={`${styles["text-opacity-w-50"]} form-control`}
                                   id="email-input"
                                   name={"email"}
                                   value={this.state.email}
                                   placeholder="example@example.org" onChange={this.handleChange}/>
                            <label htmlFor="password-input">Email</label>
                        </div>
                        <div className={"p-4"}></div>
                        <button className={`${styles["text-opacity-w-75"]} w-100 btn btn-lg btn-primary`}
                                type="submit">Register
                        </button>
                    </form>
                </div>
            </>
        )
    }
}

export function Register(props) {
    return (
        <>
            <Helmet>
                <title>Register | Lingu Image Hosting</title>
            </Helmet>
            <RegisterForm url={props.url}/>
        </>
    );
}