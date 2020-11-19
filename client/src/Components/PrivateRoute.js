import React from "react";
import {Route, Redirect} from "react-router-dom";

/**
 * custom route
 * @param Component
 * @param isUserAuthenticated
 * @param rest
 * @returns {JSX.Element}
 * @constructor
 */
const PrivateRoute = ({component: Component, isUserAuthenticated, ...rest}) => {
    return (
        <Route {...rest} render={props => (
            isUserAuthenticated ? <Component {...props} /> : <Redirect to="/login"/>
        )}/>
    );
};

export default PrivateRoute;