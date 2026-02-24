import Link from "next/link";
import { SectionHeading } from "@/components/site/section-heading";

type FaqItem = { q: string; a: string };

export function FaqSection({
  title,
  items,
  contactHref = "/lien-he",
}: {
  title: string;
  items: FaqItem[];
  contactHref?: string;
}) {
  return (
    <section className="space-y-6">
      <SectionHeading title={title} />
      <div className="space-y-2">
        {items.map((it) => (
          <details
            key={it.q}
            className="group rounded-xl border border-white/10 bg-white/5 px-5 py-4 text-white"
          >
            <summary className="cursor-pointer list-none select-none text-sm font-medium text-white/90 md:text-base">
              <span className="inline-flex items-center justify-between gap-4">
                {it.q}
                <span className="text-white/60 transition group-open:rotate-45">
                  +
                </span>
              </span>
            </summary>
            <div className="mt-3 text-sm text-white/70">{it.a}</div>
          </details>
        ))}
      </div>
      <div className="pt-2 text-center">
        <Link
          href={contactHref}
          className="inline-flex items-center justify-center rounded-full bg-gradient-to-r from-pink-500 to-orange-400 px-6 py-3 text-sm font-semibold text-white shadow-lg transition hover:from-pink-600 hover:to-orange-500"
        >
          Liên hệ hỗ trợ →
        </Link>
      </div>
    </section>
  );
}

