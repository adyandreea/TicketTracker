import { jwtDecode } from "jwt-decode";

const HasRole = ({ allowedRoles, children, fallback = null }) => {
  const token = localStorage.getItem("token");

  if (!token) return fallback;

  try {
    const decoded = jwtDecode(token);
    const userRole = decoded.roles || ""; 
    const hasAccess = allowedRoles.some(role => 
      userRole === role || userRole === `ROLE_${role}`
    );

    return hasAccess ? children : fallback;
  } catch (error) {
    console.error("JWT Decode Error:", error);
    return fallback;
  }
};

export default HasRole;