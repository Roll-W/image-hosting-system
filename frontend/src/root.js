import React, {Component, createContext, createRef} from "react";
import styles from "./index.module.css"
import logo from './logo.svg';
import {Link, Outlet} from "react-router-dom";
import {Footer} from "./pages/footer";
import {getCurrentUser} from "./action/userAction";
import {AutoHideToast} from "./action/toast";

class UserTitle extends Component {
    constructor(props) {
        super(props);
        this.state = {
            username: null,
        }
    }

    componentDidMount() {
        getCurrentUser("http://localhost:3080")
            .then(r => console.log(r))
    }

    render() {
        return (
            <div className={"dropdown text-end"}>
                <a href="#" className={"d-block link-light text-decoration-none dropdown-toggle"}
                   data-bs-toggle="dropdown" aria-expanded="false">
                    Admin
                </a>
                <ul className={"dropdown-menu text-small"}>
                    <li><a className={"dropdown-item"} href="#">Upload Image</a></li>
                    <li><a className={"dropdown-item"} href="#">Settings</a></li>
                    <li><a className={"dropdown-item"} href="#">Profile</a></li>
                    <li>
                        <hr className="dropdown-divider"/>
                    </li>
                    <li>
                        <button className="dropdown-item" id={"logout"}>Logout</button>
                    </li>
                </ul>
            </div>
        );
    }
}

export const ToastMessageContext = createContext({
    setShow: (show) => {
    },
    show: false
})

const events = (function () {
    let topics = {};
    let hOP = topics.hasOwnProperty;

    return {
        subscribe: function (topic, listener) {
            // Create the topic's object if not yet created
            if (!hOP.call(topics, topic)) topics[topic] = [];

            // Add the listener to queue
            let index = topics[topic].push(listener) - 1;

            // Provide handle back for removal of topic
            return {
                remove: function () {
                    delete topics[topic][index];
                }
            };
        },
        publish: function (topic, info) {
            // If the topic doesn't exist, or there's no listeners in queue, just leave
            if (!hOP.call(topics, topic)) return;

            // Cycle through topics queue, fire!
            topics[topic].forEach(function (item) {
                item(info !== undefined ? info : {});
            });
        }
    };
})();

export function addMessage(message) {
    events.publish('toast', {show: true, message: message})
}

class Body extends Component {
    constructor(props) {
        super(props);
        Body.contextType = ToastMessageContext
        this.state = {
            show: false,
            message: "",
            setShow: (show) => {
                this.setState({show: show})
            },
        }
        events.subscribe('toast', res => {
            console.log(res)
            this.setState({
                show: res.show,
                message: res.message
            })
        })
    }


    render() {
        return (
            <ToastMessageContext.Provider value={{setShow: this.state.setShow, show: this.state.show}}>
                <AutoHideToast show={this.state.show} message={this.state.message}/>
            </ToastMessageContext.Provider>
        )
    }
}

export class Root extends Component {
    constructor(props) {
        super(props);
    }


    render() {
        return (
            <>
                <div className={styles["body-container"]}>
                    <header className={`${styles.header} sticky-top p-3 mb-3 rounded-3`}>
                        <div className={"container text-light"}>
                            <div
                                className={"d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start"}>
                                <a href="/"
                                   className="d-flex align-items-center mb-2 mb-lg-0 text-dark text-decoration-none">
                                    <img src={logo} width={32} height={32} alt={"logo"}/>
                                </a>

                                <ul className={"nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0"}>
                                    <li><Link className={"nav-link px-2 link-primary"}
                                              to={'/'}>Home</Link></li>
                                    <li><Link className={"nav-link px-2 link-light"}
                                              to={'/about'}>About</Link></li>
                                    <li><Link className="nav-link px-2 link-light" to={'/upload'}>Upload</Link></li>
                                </ul>

                                <UserTitle/>
                            </div>
                        </div>
                    </header>
                    <div id="detail" className="pb-6">
                        <Body/>
                        <Outlet/>
                    </div>
                    <Footer/>
                </div>
            </>

        );
    }
}

export default Root;
