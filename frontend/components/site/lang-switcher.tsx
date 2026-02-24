"use client";

import * as React from "react";

type Lang = "vi" | "en";

function readLangCookie(): Lang {
  const match = document.cookie.match(/(?:^|;\s*)lang=([^;]+)/);
  return match?.[1] === "en" ? "en" : "vi";
}

function setLangCookie(lang: Lang) {
  document.cookie = `lang=${lang}; path=/; max-age=${60 * 60 * 24 * 365}`;
}

export function LangSwitcher() {
  const [lang, setLang] = React.useState<Lang>("vi");

  React.useEffect(() => {
    setLang(readLangCookie());
  }, []);

  return (
    <div className="inline-flex items-center rounded-full border border-white/15 bg-white/5 p-1 text-xs text-white/80">
      {(["vi", "en"] as const).map((l) => (
        <button
          key={l}
          type="button"
          onClick={() => {
            setLangCookie(l);
            setLang(l);
            window.location.reload();
          }}
          className={[
            "rounded-full px-3 py-1 transition",
            lang === l ? "bg-white/15 text-white" : "hover:bg-white/10",
          ].join(" ")}
        >
          {l.toUpperCase()}
        </button>
      ))}
    </div>
  );
}

