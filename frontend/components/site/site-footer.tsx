import Image from "next/image";
import Link from "next/link";
import { cookies } from "next/headers";
import {
  Facebook,
  Instagram,
  Mail,
  MessageCircle,
  Globe,
  Youtube,
} from "lucide-react";

import {
  getCompany,
  getLegal,
  pickLocalized,
  type Lang,
} from "@/lib/server/data";

async function getLang(): Promise<Lang> {
  const value = (await cookies()).get("lang")?.value;
  return value === "en" ? "en" : "vi";
}

export async function SiteFooter() {
  const lang = await getLang();
  const [legal, company] = await Promise.all([getLegal(), getCompany()]);
  const pages = legal.legalPages;

  const privacy = pages.find((p) => p.slug === "privacy-policy");
  const terms = pages.find((p) => p.slug === "terms-and-conditions");

  const social = company.social ?? {};
  const year = new Date().getFullYear();

  const socialLinks = [
    {
      key: "facebook",
      href: social.facebook?.value,
      icon: <Facebook className="size-4" />,
      label: "Facebook",
    },
    {
      key: "zalo",
      href: social.zalo?.value,
      icon: <MessageCircle className="size-4" />,
      label: "Zalo",
    },
    {
      key: "instagram",
      href: social.instagram?.value,
      icon: <Instagram className="size-4" />,
      label: "Instagram",
    },
    {
      key: "youtube",
      href: social.youtube?.value,
      icon: <Youtube className="size-4" />,
      label: "YouTube",
    },
  ].filter((s) => !!s.href);

  return (
    <footer className="border-t bg-card">
      <div className="mx-auto max-w-screen-xl px-4 py-10">
        {/* Top multi-column section */}
        <div className="grid gap-10 pb-8 md:grid-cols-4 lg:grid-cols-5">
          {/* Brand + certification */}
          <div className="space-y-4">
            <Image
              src="/next.svg"
              alt="CineLove"
              width={120}
              height={32}
              className="h-7 w-auto"
            />
            <p className="text-sm text-muted-foreground">
              {lang === "vi"
                ? "Nền tảng thiệp online & website cưới hiện đại, dễ dùng."
                : "Modern online invitations and wedding sites made simple."}
            </p>
            <div className="flex flex-wrap items-center gap-3">
              <Image
                src="/logo-da-thong-bao-bo-cong-thuong.webp"
                alt="Đã thông báo Bộ Công Thương"
                width={120}
                height={40}
                className="h-10 w-auto"
              />
            </div>
          </div>

          {/* Column: Sản phẩm */}
          <div className="space-y-3">
            <h3 className="text-sm font-semibold text-foreground">
              {lang === "vi" ? "Sản phẩm" : "Product"}
            </h3>
            <ul className="space-y-2 text-sm text-muted-foreground">
              <li>
                <Link href="/mau-thiep" className="hover:text-foreground">
                  {lang === "vi" ? "Mẫu thiệp" : "Templates"}
                </Link>
              </li>
              <li>
                <Link href="/goi-dich-vu" className="hover:text-foreground">
                  {lang === "vi" ? "Gói dịch vụ" : "Service plans"}
                </Link>
              </li>
              <li>
                <Link href="/thiep-da-tao" className="hover:text-foreground">
                  {lang === "vi" ? "Thiệp đã tạo" : "Created invitations"}
                </Link>
              </li>
            </ul>
          </div>

          {/* Column: Nội dung & cảm hứng */}
          <div className="space-y-3">
            <h3 className="text-sm font-semibold text-foreground">
              {lang === "vi" ? "Nội dung" : "Content"}
            </h3>
            <ul className="space-y-2 text-sm text-muted-foreground">
              <li>
                <Link href="/news" className="hover:text-foreground">
                  {lang === "vi" ? "Tin tức & khuyến mãi" : "News & promos"}
                </Link>
              </li>
              <li>
                <Link href="/blogs" className="hover:text-foreground">
                  {lang === "vi" ? "Blog" : "Blog"}
                </Link>
              </li>
              <li>
                <Link href="/lien-he" className="hover:text-foreground">
                  {lang === "vi" ? "Liên hệ" : "Contact"}
                </Link>
              </li>
            </ul>
          </div>

          {/* Column: Chính sách (legal) */}
          <div className="space-y-3 lg:col-span-2">
            <h3 className="text-sm font-semibold text-foreground">
              {lang === "vi" ? "Chính sách" : "Policies"}
            </h3>
            <div className="grid gap-2 text-sm text-muted-foreground sm:grid-cols-2">
              {pages.map((page) => (
                <Link
                  key={page.id}
                  href={`/${page.slug}`}
                  className="hover:text-foreground"
                >
                  {pickLocalized(page.label, lang)}
                </Link>
              ))}
            </div>
          </div>
        </div>

        {/* Bottom bar */}
        <div className="mt-4 flex flex-col gap-4 border-t pt-4 text-xs text-muted-foreground md:flex-row md:items-center md:justify-between">
          <div className="flex flex-wrap items-center gap-4">
            <div className="flex items-center gap-2">
              <Globe className="size-4" />
              <span>
                {lang === "vi"
                  ? "Tiếng Việt (Việt Nam)"
                  : "English (International)"}
              </span>
            </div>
            {socialLinks.length > 0 && (
              <div className="flex items-center gap-3">
                {socialLinks.map((s) => (
                  <Link
                    key={s.key}
                    href={s.href!}
                    target="_blank"
                    rel="noopener noreferrer"
                    aria-label={s.label}
                    className="text-muted-foreground hover:text-foreground"
                  >
                    {s.icon}
                  </Link>
                ))}
                {social.email?.value && (
                  <Link
                    href={`mailto:${social.email.value}`}
                    aria-label="Email"
                    className="text-muted-foreground hover:text-foreground"
                  >
                    <Mail className="size-4" />
                  </Link>
                )}
              </div>
            )}
          </div>

          <div className="flex flex-wrap items-center gap-3 md:justify-end">
            {privacy && (
              <Link
                href={`/${privacy.slug}`}
                className="hover:text-foreground"
              >
                {pickLocalized(privacy.label, lang)}
              </Link>
            )}
            {terms && (
              <>
                <span>·</span>
                <Link
                  href={`/${terms.slug}`}
                  className="hover:text-foreground"
                >
                  {pickLocalized(terms.label, lang)}
                </Link>
              </>
            )}
            <span>·</span>
            <span>© {year} CineLove</span>
          </div>
        </div>
      </div>
    </footer>
  );
}

