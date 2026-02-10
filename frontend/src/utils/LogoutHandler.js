import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

const LogoutHandler = () => {
  const navigate = useNavigate();

  useEffect(() => {
    localStorage.clear();
    sessionStorage.clear();

    navigate("/login", { replace: true });
  }, [navigate]);

  return null;
};

export default LogoutHandler;
