import { cookies } from "next/headers";

import { SectionHeading } from "@/components/site/section-heading";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { getProducts, pickLocalized, type Lang } from "@/lib/server/data";

async function getLang(): Promise<Lang> {
  const value = (await cookies()).get("lang")?.value;
  return value === "en" ? "en" : "vi";
}

function formatPriceVnd(value: number): string {
  return new Intl.NumberFormat("vi-VN").format(value) + "đ";
}

export default async function GoiDichVuPage() {
  const lang = await getLang();
  const products = await getProducts();
  const services = products.filter((p) => p.type === "service" && p.isActive);

  return (
    <div className="space-y-8 pb-14">
      <SectionHeading
        title={lang === "vi" ? "Gói dịch vụ" : "Service packages"}
        description={
          lang === "vi"
            ? "Khám phá các gói dịch vụ phù hợp nhu cầu và ngân sách."
            : "Explore packages that fit your needs and budget."
        }
      />

      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
        {services.map((p) => (
          <Card key={p.id} className="border-white/10 bg-white/5 text-white">
            <CardHeader className="space-y-1">
              <CardTitle className="line-clamp-2 text-base">
                {pickLocalized(p.name, lang)}
              </CardTitle>
              <div className="text-sm text-white/60">
                {formatPriceVnd(p.price)}
              </div>
            </CardHeader>
            <CardContent className="text-sm text-white/70">
              <p className="line-clamp-4">{pickLocalized(p.description, lang)}</p>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  );
}

