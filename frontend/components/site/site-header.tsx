import Image from "next/image";
import Link from "next/link";

import { Button } from "@/components/ui/button";
import { LangSwitcher } from "@/components/site/lang-switcher";
import { ThemeSwitcher } from "@/components/site/theme-switcher";

type NavItem = { href: string; label: string };

export function SiteHeader() {
  const nav: NavItem[] = [
    { href: "/", label: "Trang chủ" },
    { href: "/goi-dich-vu", label: "Gói dịch vụ" },
    { href: "/mau-thiep", label: "Mẫu thiệp" },
    { href: "/thiep-da-tao", label: "Thiệp đã tạo" },
    { href: "/news", label: "Tin tức" },
    { href: "/blogs", label: "Blogs" },
    { href: "/lien-he", label: "Liên hệ" },
  ];

  return (
    <header className="fixed inset-x-0 top-0 z-50 border-b border-border bg-background/80 backdrop-blur">
      <div className="mx-auto flex h-16 max-w-screen-xl items-center justify-between gap-4 px-4">
        <Link href="/" className="flex items-center gap-2">
          <Image
            src="/next.svg"
            alt="CineLove"
            width={120}
            height={32}
            className="h-7 w-auto"
            priority
          />
        </Link>

        <nav className="hidden items-center gap-6 text-sm text-foreground/80 md:flex">
          {nav.map((item) => (
            <Link
              key={item.href}
              href={item.href}
              className="transition hover:text-foreground"
            >
              {item.label}
            </Link>
          ))}
        </nav>

        <div className="flex items-center gap-2">
          <ThemeSwitcher />
          <LangSwitcher />
          <Button variant="secondary" className="hidden md:inline-flex">
            Đăng nhập
          </Button>
          <Button className="bg-gradient-to-r from-pink-500 to-orange-400 text-white hover:from-pink-600 hover:to-orange-500">
            Bắt đầu
          </Button>
        </div>
      </div>
    </header>
  );
}

