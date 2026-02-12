import React, { createContext, useState, useContext, useEffect } from "react";
import translationsData from "./i18n.json";

const LanguageContext = createContext();

export const LanguageProvider = ({ children }) => {
  const savedLanguage = localStorage.getItem("app_language") || "ENG";
  const [currentLanguage, setCurrentLanguage] = useState(savedLanguage);

  useEffect(() => {
    localStorage.setItem("app_language", currentLanguage);
  }, [currentLanguage]);

  const translate = (key) => {
    return translationsData.translation[key]?.[currentLanguage] || key;
  };

  return (
    <LanguageContext.Provider
      value={{
        currentLanguage,
        changeLanguage: setCurrentLanguage,
        translate,
        availableLanguages: translationsData.languages,
      }}
    >
      {children}
    </LanguageContext.Provider>
  );
};

export const useLanguage = () => useContext(LanguageContext);
