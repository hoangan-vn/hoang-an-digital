import Link from "next/link";

import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { SectionHeading } from "@/components/site/section-heading";
import { TemplateCarousel } from "@/components/site/template-carousel";
import { FaqSection } from "@/components/site/faq-section";
import {
  getBlogs,
  getPartners,
  getProducts,
  getPromotions,
  getVouchers,
  pickLocalized,
  type Lang,
} from "@/lib/server/data";

import { cookies } from "next/headers";

async function getLang(): Promise<Lang> {
  const value = (await cookies()).get("lang")?.value;
  return value === "en" ? "en" : "vi";
}

function formatPriceVnd(value: number): string {
  return new Intl.NumberFormat("vi-VN").format(value) + "đ";
}

export async function HomeDynamicSections() {
  const lang = await getLang();

  const [products, partners, blogs, promotions, vouchers] = await Promise.all([
    getProducts(),
    getPartners(),
    getBlogs(),
    getPromotions(),
    getVouchers(),
  ]);

  const inviteTemplates = products
    .filter((p) => p.categoryId === "cat_invitation" && p.isActive)
    .slice(0, 8);

  const servicePackages = products
    .filter((p) => p.type === "service" && p.isActive)
    .slice(0, 6);

  const featuredBlogs = blogs
    .filter((b) => b.isFeatured)
    .sort((a, b) => b.publishedAt.localeCompare(a.publishedAt))
    .slice(0, 3);

  const activePromotions = promotions.filter((p) => p.isActive).slice(0, 2);
  const activeVouchers = vouchers.filter((v) => v.isActive).slice(0, 2);

  const partnerHighlights = partners.filter((p) => p.isActive).slice(0, 3);

  return (
    <>
      <section className="space-y-6">
        <SectionHeading
          title={lang === "vi" ? "Khuyến mãi nổi bật" : "Featured promotions"}
          description={
            lang === "vi"
              ? "Giảm giá trực tiếp trên sản phẩm hoặc dùng voucher theo điều kiện."
              : "Instant discounts on products or conditional vouchers."
          }
          rightSlot={
            <Link
              href="/goi-dich-vu"
              className="text-sm font-medium text-white/80 hover:text-white"
            >
              Xem thêm →
            </Link>
          }
        />
        <div className="grid gap-4 md:grid-cols-2">
          {activePromotions.map((promo) => (
            <Card
              key={promo.id}
              className="border-white/10 bg-white/5 text-white"
            >
              <CardHeader>
                <CardTitle className="text-lg">
                  {pickLocalized(promo.name, lang)}
                </CardTitle>
              </CardHeader>
              <CardContent className="text-sm text-white/70">
                {promo.value.unit === "percent"
                  ? `${promo.value.amount}%`
                  : formatPriceVnd(promo.value.amount)}{" "}
                {lang === "vi" ? "giảm trực tiếp" : "instant discount"}
              </CardContent>
            </Card>
          ))}
          {activeVouchers.map((voucher) => (
            <Card
              key={voucher.id}
              className="border-white/10 bg-white/5 text-white"
            >
              <CardHeader>
                <CardTitle className="text-lg">
                  {pickLocalized(voucher.name, lang)}
                </CardTitle>
              </CardHeader>
              <CardContent className="text-sm text-white/70">
                Code: <span className="font-semibold">{voucher.code}</span> •{" "}
                {lang === "vi" ? "Tối thiểu" : "Min"}{" "}
                {formatPriceVnd(voucher.conditions.minOrderValue)}
              </CardContent>
            </Card>
          ))}
        </div>
      </section>

      <section className="space-y-6">
        <SectionHeading
          title={lang === "vi" ? "Mẫu thiệp nổi bật" : "Featured templates"}
          description={
            lang === "vi"
              ? "Chọn mẫu, tuỳ chỉnh, chia sẻ ngay. Hỗ trợ RSVP, bản đồ, thư viện ảnh."
              : "Pick a template, customize, and share instantly. RSVP, maps, gallery."
          }
          rightSlot={
            <Link
              href="/mau-thiep"
              className="text-sm font-medium text-white/80 hover:text-white"
            >
              {lang === "vi" ? "Xem tất cả →" : "View all →"}
            </Link>
          }
        />
        <TemplateCarousel
          items={inviteTemplates.map((p) => ({
            id: p.id,
            title: pickLocalized(p.name, lang),
            description: pickLocalized(p.description, lang),
            priceLabel: formatPriceVnd(p.price),
            href: "/mau-thiep",
          }))}
        />
      </section>

      <section className="space-y-6">
        <SectionHeading
          title={lang === "vi" ? "Gói dịch vụ" : "Service packages"}
          description={
            lang === "vi"
              ? "Gợi ý dịch vụ nổi bật từ đối tác: sảnh tiệc, chụp ảnh, makeup..."
              : "Featured partner services: venue, photography, makeup..."
          }
          rightSlot={
            <Link
              href="/goi-dich-vu"
              className="text-sm font-medium text-white/80 hover:text-white"
            >
              {lang === "vi" ? "Xem thêm →" : "Explore →"}
            </Link>
          }
        />
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {servicePackages.map((p) => (
            <Card
              key={p.id}
              className="border-white/10 bg-white/5 text-white"
            >
              <CardHeader className="space-y-1">
                <CardTitle className="line-clamp-2 text-base">
                  {pickLocalized(p.name, lang)}
                </CardTitle>
                <div className="text-sm text-white/60">
                  {formatPriceVnd(p.price)}
                </div>
              </CardHeader>
              <CardContent className="text-sm text-white/70">
                <p className="line-clamp-3">{pickLocalized(p.description, lang)}</p>
              </CardContent>
            </Card>
          ))}
        </div>
      </section>

      <section className="space-y-6">
        <SectionHeading
          title={lang === "vi" ? "Blog mới" : "Latest blog posts"}
          description={
            lang === "vi"
              ? "Chia sẻ kinh nghiệm thiệp online, cưới hỏi, và feedback khách hàng."
              : "Insights on invitations, weddings, and customer feedback."
          }
          rightSlot={
            <Link
              href="/blogs"
              className="text-sm font-medium text-white/80 hover:text-white"
            >
              {lang === "vi" ? "Xem blog →" : "Read blog →"}
            </Link>
          }
        />
        <div className="grid gap-4 md:grid-cols-3">
          {featuredBlogs.map((b) => (
            <Card
              key={b.id}
              className="border-white/10 bg-white/5 text-white"
            >
              <CardHeader className="space-y-1">
                <CardTitle className="line-clamp-2 text-base">
                  {pickLocalized(b.title, lang)}
                </CardTitle>
                <div className="text-xs text-white/55">{b.publishedAt.slice(0, 10)}</div>
              </CardHeader>
              <CardContent className="text-sm text-white/70">
                <p className="line-clamp-3">{pickLocalized(b.excerpt, lang)}</p>
              </CardContent>
            </Card>
          ))}
        </div>
      </section>

      <section className="space-y-6">
        <SectionHeading
          title={lang === "vi" ? "Đối tác kinh doanh" : "Partners"}
          description={
            lang === "vi"
              ? "Các đối tác được tuyển chọn để mang đến trải nghiệm tốt nhất."
              : "Curated partners to deliver the best experience."
          }
        />
        <div className="grid gap-4 md:grid-cols-3">
          {partnerHighlights.map((p) => (
            <Card
              key={p.id}
              className="border-white/10 bg-white/5 text-white"
            >
              <CardHeader className="space-y-1">
                <CardTitle className="text-base">
                  {pickLocalized(p.name, lang)}
                </CardTitle>
                <div className="text-sm text-white/60">
                  {p.typeLabel ? pickLocalized(p.typeLabel, lang) : p.type}
                </div>
              </CardHeader>
              <CardContent className="text-sm text-white/70">
                <p className="line-clamp-3">
                  {p.shortDescription ? pickLocalized(p.shortDescription, lang) : ""}
                </p>
              </CardContent>
            </Card>
          ))}
        </div>
      </section>

      <FaqSection
        title={lang === "vi" ? "Câu hỏi thường gặp" : "FAQ"}
        items={[
          {
            q:
              lang === "vi"
                ? "Thiệp online có hỗ trợ RSVP không?"
                : "Does the invitation support RSVP?",
            a:
              lang === "vi"
                ? "Có. Bạn có thể theo dõi trạng thái tham dự (đồng ý/từ chối/chờ) theo từng nhóm khách mời."
                : "Yes. You can track attendance status per guest group (attending/declined/pending).",
          },
          {
            q:
              lang === "vi"
                ? "Có thể nhúng video YouTube vào thiệp không?"
                : "Can I embed a YouTube video?",
            a:
              lang === "vi"
                ? "Tuỳ mẫu/gói, bạn có thể thêm liên kết YouTube để hiển thị trên thiệp."
                : "Depending on the template/package, you can add a YouTube link to show on the invitation.",
          },
          {
            q:
              lang === "vi"
                ? "Voucher/Coupon áp dụng như thế nào?"
                : "How do vouchers work?",
            a:
              lang === "vi"
                ? "Voucher giảm theo % hoặc số tiền cố định khi đủ điều kiện (ví dụ đơn tối thiểu)."
                : "Vouchers can be percentage or fixed discounts with conditions (e.g. minimum order value).",
          },
        ]}
      />
    </>
  );
}

