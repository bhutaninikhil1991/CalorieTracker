import jwt_decode from "jwt-decode";

/**
 * get user Id by reading token
 * @returns {number}
 */
export const getUserId = () => {
    let token = jwt_decode(localStorage.getItem("token"));
    let userId = parseInt(token.sub);
    return userId;
}