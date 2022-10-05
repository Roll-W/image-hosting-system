import React from 'react';
import ReactDOM from 'react-dom/client';
import Root from './root';
import reportWebVitals from './reportWebVitals';
import {
    createBrowserRouter,
    RouterProvider,
    Route,
} from "react-router-dom";

import ErrorPage from "./pages/error-page";
import About from "./pages/about"
import Home from "./pages/home";
import {Upload} from "./pages/upload";
import {Login} from "./pages/login";

const url = "http://localhost:3080"

const router = createBrowserRouter([
    {
        path: "/",
        element: <Root/>,
        errorElement: <ErrorPage/>,
        children: [
            {
                path: "about",
                element: <About/>
            },
            {
                path: "/",
                element: <Home/>
            },
            {
                path: "upload",
                element: <Upload />
            },
            {
                path: "profile",
            },
            {
                path: "login",
                element: <Login url={url}/>
            },
            {
                path: "register",
                element: <Login url={url}/>
            },

        ]
    },
]);

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <React.StrictMode>
        <RouterProvider router={router}/>
    </React.StrictMode>
);

// reportWebVitals(console.log);
