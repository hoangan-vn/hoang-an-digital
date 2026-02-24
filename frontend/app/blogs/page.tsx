import { cookies } from "next/headers";

import { SectionHeading } from "@/components/site/section-heading";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { getBlogs, pickLocalized, type Lang } from "@/lib/server/data";

async function getLang(): Promise<Lang> {
  const value = (await cookies()).get("lang")?.value;
  return value === "en" ? "en" : "vi";
}

export default async function BlogsPage() {
  const lang = await getLang();
  const blogs = await getBlogs();

  const sorted = [...blogs].sort((a, b) => b.publishedAt.localeCompare(a.publishedAt));

  return (
    <div className="space-y-8 pb-14">
      <SectionHeading
        title={lang === "vi" ? "Blogs" : "Blog"}
        description={
          lang === "vi"
            ? "Chia sẻ về thiệp online, cưới hỏi và đối tác."
            : "Posts about online invitations, weddings, and partners."
        }
      />

      <div className="grid gap-4 md:grid-cols-3">
        {sorted.map((b) => (
          <Card key={b.id} className="border-white/10 bg-white/5 text-white">
            <CardHeader className="space-y-1">
              <CardTitle className="line-clamp-2 text-base">
                {pickLocalized(b.title, lang)}
              </CardTitle>
              <div className="text-xs text-white/55">{b.publishedAt.slice(0, 10)}</div>
            </CardHeader>
            <CardContent className="text-sm text-white/70">
              <p className="line-clamp-4">{pickLocalized(b.excerpt, lang)}</p>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  );
}

