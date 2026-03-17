import { Geist, Geist_Mono } from "next/font/google";
import "./globals.css";
import { SiteHeader } from "@/components/site/site-header";
import { SiteFooter } from "@/components/site/site-footer";
import { StickyContactBar } from "@/components/site/sticky-contact-bar";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="vi" suppressHydrationWarning>
      <body
        className={`${geistSans.variable} ${geistMono.variable} min-h-dvh antialiased bg-background text-foreground`}
      >
        <script
          dangerouslySetInnerHTML={{
            __html: `(function(){var t=localStorage.getItem('theme');if(t==='dark')document.documentElement.classList.add('dark');else if(t!=='light')localStorage.setItem('theme','light');})();`,
          }}
        />
        <SiteHeader />
        <main className="mx-auto w-full max-w-screen-xl px-4 pt-20">
          {children}
        </main>
        <SiteFooter />
        <StickyContactBar
          facebookUrl="https://facebook.com/hoangan.digital"
          zaloUrl="https://zalo.me/"
          phone="+84 912 345 678"
        />
      </body>
    </html>
  );
}
