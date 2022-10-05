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
import React, {Component} from 'react';
import Col from "react-bootstrap/Col";
import Toast from "react-bootstrap/Toast";
import Row from "react-bootstrap/Row";
import {ToastContainer} from "react-bootstrap";
import styles from '../index.module.css'

export function createToast(title, message) {
    console.log(`${title}  ${message}`)
    SetText.show({ onOk: this.handleOk });
}

class Modal extends Component {
    render() {
        return (
            <Row>
                <Col>
                    <ToastContainer className={`${styles.top} ${styles["top-25"]}`} position={"top-center"}>
                        <Toast
                            show={true} delay={3000} autohide>
                            <Toast.Body>Login failure</Toast.Body>
                        </Toast>
                    </ToastContainer>
                </Col>
            </Row>
        )
    }
}

const withDialog = WrappedComponent => {
    function EnhancedComponent(props) {
        const {title, onClose, ...others} = props;
        return (
            <Modal visible={true} title={title || WrappedComponent.title} footer={<div/>}>
                <WrappedComponent {...others} onClose={onClose}/>
            </Modal>
        );
    }

    EnhancedComponent.show = params => {
        let container = document.createElement("div");
        document.body.appendChild(container);

        function closeHandle() {
            ReactDOM.unmountComponentAtNode(container);
            document.body.removeChild(container);
            container = null;
        }

        ReactDOM.render(<EnhancedComponent {...params} onClose={closeHandle}/>, container);
    };

    return EnhancedComponent;
};

@withDialog // 使用高阶组件，很关键
class SetText extends React.Component {
    static title = "设置文案";

    static defaultProps = {
        onClose: () => {}
    };

    constructor(props) {
        super(props);
        this.state = { text: "" };
    }

    onChange = e => {
        this.setState({ text: e.target.value });
    };

    handleOk = () => {
        this.props.onOk(this.state.text);
        this.props.onClose();
    };

    render() {
        return (<div>
            <Input value={this.state.text} onChange={this.onChange} />
            <div>
                <Button onClick={this.handleOk}>确定</Button>
                <Button onClick={this.props.onClose}>取消</Button>
            </div>
        </div>);
    }
}

export default Modal;