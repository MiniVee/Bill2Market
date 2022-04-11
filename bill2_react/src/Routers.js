import MainPage from "./Main/Main"
import {BrowserRouter, Route, Routes} from "react-router-dom";
import LoginPage from "./login/login";
import SignUpPage from "./signUp/signUp";
import SnsSignUpPage from "./signUp/snsSignUp";

function Routers() {
    return (
        <div>
            <BrowserRouter>
                <Routes>
                    <Route path = "/*" element={<MainPage />} />
                    <Route path = "/login" element={<LoginPage />} />
                    <Route path = "/snsSignUp" element={<SnsSignUpPage />} />
                    <Route path = "/signUp" element={<SignUpPage />} />
                </Routes>
            </BrowserRouter>
        </div>
    );
}

export default Routers;