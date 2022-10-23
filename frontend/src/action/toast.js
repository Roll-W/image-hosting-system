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
import React from 'react';
import Col from "react-bootstrap/Col";
import Toast from "react-bootstrap/Toast";
import Row from "react-bootstrap/Row";
import {ToastContainer} from "react-bootstrap";
import styles from '../index.module.css'
import {addMessage, ToastMessageContext} from "../root";

export function createToast(title, message) {
    console.log(`${title}  ${message}`)
    //alert(message)
    addMessage(message)
}


export function AutoHideToast(props) {
    return (
        <ToastMessageContext.Consumer>
            {
                value => (
                    <Row>
                        <Col>
                            <ToastContainer className={`${styles.top} ${styles["top-25"]}`} position={"top-center"}>
                                <Toast
                                    onClose={() => value.setShow(false)} show={value.show} delay={3000} autohide>
                                    <Toast.Body>{props.message}</Toast.Body>
                                </Toast>
                            </ToastContainer>
                        </Col>
                    </Row>
                )
            }
        </ToastMessageContext.Consumer>

    )
}
