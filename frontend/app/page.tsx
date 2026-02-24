import Link from "next/link";

export default function Home() {
  return (
    <div className="space-y-14 pb-14">
      <section className="relative overflow-hidden rounded-2xl border border-white/10 bg-gradient-to-br from-white/5 via-white/0 to-white/5">
        <div className="absolute inset-0 bg-[radial-gradient(circle_at_20%_20%,rgba(236,72,153,0.35),transparent_40%),radial-gradient(circle_at_80%_30%,rgba(249,115,22,0.25),transparent_45%)]" />
        <div className="relative grid gap-8 p-8 md:grid-cols-2 md:p-12">
          <div className="flex flex-col justify-center">
            <h1 className="text-balance text-4xl font-semibold tracking-tight md:text-5xl">
              CINELOVE
              <br />
              <span className="bg-gradient-to-r from-pink-400 to-orange-300 bg-clip-text text-transparent">
                Make your love story
              </span>
            </h1>
            <p className="mt-4 max-w-xl text-pretty text-white/75">
              Hãy để CineLove giúp bạn kể câu chuyện tình yêu qua từng tấm thiệp
              – mang đầy cảm xúc như một thước phim lãng mạn.
            </p>
            <div className="mt-6 flex flex-wrap gap-3">
              <Link
                href="/mau-thiep"
                className="inline-flex items-center justify-center rounded-full bg-gradient-to-r from-pink-500 to-orange-400 px-6 py-3 text-sm font-semibold text-white shadow-lg transition hover:from-pink-600 hover:to-orange-500"
              >
                Bắt đầu →
              </Link>
              <Link
                href="/goi-dich-vu"
                className="inline-flex items-center justify-center rounded-full border border-white/15 bg-white/5 px-6 py-3 text-sm font-semibold text-white/90 transition hover:bg-white/10"
              >
                Xem gói dịch vụ
              </Link>
            </div>
          </div>

          <div className="relative">
            <div className="aspect-[4/3] w-full overflow-hidden rounded-xl border border-white/10 bg-black/20">
              {/* Placeholder image block (swap to real design assets later) */}
              <div className="h-full w-full bg-[radial-gradient(circle_at_30%_30%,rgba(255,255,255,0.12),transparent_55%),linear-gradient(135deg,rgba(236,72,153,0.25),rgba(249,115,22,0.15))]" />
            </div>
            <div className="pointer-events-none absolute -bottom-6 -right-6 hidden h-48 w-40 rotate-6 rounded-xl border border-white/10 bg-white/5 shadow-2xl md:block" />
          </div>
        </div>
      </section>

      <HomeDynamicSections />
    </div>
  );
}

import { HomeDynamicSections } from "@/components/site/home-dynamic-sections";
