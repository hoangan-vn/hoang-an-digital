import { cookies } from "next/headers";

import { SectionHeading } from "@/components/site/section-heading";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { getNews, pickLocalized, type Lang } from "@/lib/server/data";

async function getLang(): Promise<Lang> {
  const value = (await cookies()).get("lang")?.value;
  return value === "en" ? "en" : "vi";
}

export default async function NewsPage() {
  const lang = await getLang();
  const news = await getNews();
  const sorted = [...news].sort((a, b) => b.publishedAt.localeCompare(a.publishedAt));

  return (
    <div className="space-y-8 pb-14">
      <SectionHeading
        title={lang === "vi" ? "Tin tức" : "News"}
        description={
          lang === "vi"
            ? "Thông báo khuyến mãi, chính sách và cập nhật mới."
            : "Promotions, policies, and updates."
        }
      />

      <div className="grid gap-4 md:grid-cols-2">
        {sorted.map((n) => (
          <Card key={n.id} className="border-white/10 bg-white/5 text-white">
            <CardHeader className="space-y-1">
              <CardTitle className="line-clamp-2 text-base">
                {pickLocalized(n.title, lang)}
              </CardTitle>
              <div className="text-xs text-white/55">
                {n.type.toUpperCase()} • {n.publishedAt.slice(0, 10)}
              </div>
            </CardHeader>
            <CardContent className="text-sm text-white/70">
              <p className="line-clamp-3">{pickLocalized(n.excerpt, lang)}</p>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  );
}

