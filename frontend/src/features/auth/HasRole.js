import { jwtDecode } from "jwt-decode";

const HasRole = ({ allowedRoles, children }) => {
  const token = localStorage.getItem("token");

  if (!token) return null;

  try {
    const decoded = jwtDecode(token);
    const userRole = decoded.roles || ""; 
    const hasAccess = allowedRoles.some(role => 
      userRole === role || userRole === `ROLE_${role}`
    );

    return hasAccess ? children : null;
  } catch (error) {
    console.error("JWT Decode Error:", error);
    return null;
  }
};

export default HasRole;