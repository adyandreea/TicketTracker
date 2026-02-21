import { Navigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";

const ProtectedRoute = ({ children, allowedRoles }) => {
  const isAuthenticated = localStorage.getItem("token");

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (allowedRoles && allowedRoles.length > 0) {
    try {
      const decoded = jwtDecode(isAuthenticated);
      const userRole = decoded.roles || "";
      const hasAccess = allowedRoles.some(
        (role) => userRole === role || userRole === `ROLE_${role}`,
      );

      if (!hasAccess) {
        return <Navigate to="/dashboard" replace />;
      }
    } catch (error) {
      localStorage.removeItem("token");
      return <Navigate to="/login" replace />;
    }
  }
  return children;
};

export default ProtectedRoute;
