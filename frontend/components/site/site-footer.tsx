import Link from "next/link";
import Image from "next/image";

export function SiteFooter() {
  return (
    <footer className="border-t border-white/10 bg-black/40">
      <div className="mx-auto flex max-w-screen-xl flex-col gap-6 px-4 py-10 md:flex-row md:items-center md:justify-between">
        <div className="flex items-center gap-2">
          <Image
            src="/next.svg"
            alt="CineLove"
            width={120}
            height={32}
            className="h-7 w-auto"
          />
          <span className="text-sm text-white/60">
            © {new Date().getFullYear()} CineLove
          </span>
        </div>

        <div className="flex flex-wrap items-center gap-x-4 gap-y-2 text-sm text-white/70">
          <Link href="/privacy-policy" className="hover:text-white">
            Chính sách bảo mật
          </Link>
          <span className="text-white/25">|</span>
          <Link href="/terms-and-conditions" className="hover:text-white">
            Điều khoản dịch vụ
          </Link>
        </div>
      </div>
    </footer>
  );
}

