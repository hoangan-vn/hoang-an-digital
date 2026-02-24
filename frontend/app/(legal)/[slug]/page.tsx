import { notFound } from "next/navigation";
import { cookies } from "next/headers";

import { SectionHeading } from "@/components/site/section-heading";
import { getLegal, pickLocalized, type Lang } from "@/lib/server/data";

async function getLang(): Promise<Lang> {
  const value = (await cookies()).get("lang")?.value;
  return value === "en" ? "en" : "vi";
}

export default async function LegalPage({
  params,
}: {
  params: { slug: string };
}) {
  const lang = await getLang();
  const { slug } = params;

  const legal = await getLegal();
  const page = legal.legalPages.find((p) => p.slug === slug);
  if (!page) return notFound();

  const title = pickLocalized(page.label, lang);
  const content = page.content ? pickLocalized(page.content, lang) : "";

  return (
    <div className="space-y-8 pb-14">
      <SectionHeading title={title} />
      <div className="prose prose-invert max-w-none">
        {content ? (
          <pre className="whitespace-pre-wrap text-sm text-white/75">
            {content}
          </pre>
        ) : (
          <p className="text-sm text-white/70">
            {lang === "vi"
              ? "Nội dung đang được cập nhật."
              : "Content is being updated."}
          </p>
        )}
      </div>
    </div>
  );
}

