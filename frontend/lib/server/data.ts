import "server-only";

import path from "path";
import { readFile } from "fs/promises";

export type Lang = "vi" | "en";
export type LocalizedString = { vi: string; en: string };

export type Category = {
  id: string;
  name: LocalizedString;
  slug: LocalizedString;
  seoMetaId?: string;
  createdAt: string;
};

export type Partner = {
  id: string;
  name: LocalizedString;
  slug: LocalizedString;
  type: string;
  typeLabel?: LocalizedString;
  phone?: string;
  email?: string;
  shortDescription?: LocalizedString;
  address?: {
    full: string;
    province?: string;
    district?: string;
    ward?: string;
    geo?: { lat: number; lng: number };
  };
  logoUrl?: string;
  coverImages?: string[];
  isActive: boolean;
  seoMetaId?: string;
  createdAt: string;
};

export type Product = {
  id: string;
  name: LocalizedString;
  slug: LocalizedString;
  type: string;
  typeLabel?: LocalizedString;
  price: number;
  currency: string;
  categoryId: string;
  partnerId: string;
  promotionApplied: boolean;
  promotionIds: string[];
  thumbnailUrl: string;
  description: LocalizedString;
  images: string[];
  youtubeUrl: string | null;
  isActive: boolean;
  seoMetaId?: string;
  stats?: {
    viewCount?: number;
    likeCount?: number;
    addToCartCount?: number;
    orderCount?: number;
  };
  createdByUserId: string;
  createdAt: string;
};

export type Promotion = {
  id: string;
  name: LocalizedString;
  slug: LocalizedString;
  type: "promotion";
  value: { unit: "vnd" | "percent"; amount: number; maxDiscount?: number };
  isActive: boolean;
  validFrom?: string;
  validTo?: string;
  appliesTo: {
    productIds: string[];
    partnerIds: string[];
    categoryIds: string[];
  };
  seoMetaId?: string;
  createdAt: string;
};

export type Voucher = {
  id: string;
  name: LocalizedString;
  slug: LocalizedString;
  type: "coupon";
  value: { unit: "vnd" | "percent"; amount: number; maxDiscount?: number };
  conditions: {
    minOrderValue: number;
    applicableCategoryIds: string[];
    applicablePartnerIds: string[];
  };
  isActive: boolean;
  validFrom?: string;
  validTo?: string;
  code: string;
  usageLimit?: number;
  usedCount?: number;
  seoMetaId?: string;
  createdAt: string;
};

export type Blog = {
  id: string;
  title: LocalizedString;
  slug: LocalizedString;
  category: string;
  tags: string[];
  thumbnailUrl: string;
  excerpt: LocalizedString;
  contentMarkdown: LocalizedString;
  author: { name: string; partnerId: string | null };
  isFeatured: boolean;
  seoMetaId?: string;
  publishedAt: string;
  createdAt: string;
};

export type News = {
  id: string;
  type: "promotion" | "policy" | "product" | "partner";
  title: LocalizedString;
  slug: LocalizedString;
  excerpt: LocalizedString;
  related: Record<string, unknown>;
  seoMetaId?: string;
  publishedAt: string;
  createdAt: string;
};

export type CardCustomer = {
  cards: Array<{
    id: string;
    ownerUserId: string;
    title: LocalizedString;
    slug: LocalizedString;
    event: {
      name: LocalizedString;
      date: string;
      time: string;
      locationName: LocalizedString;
      address: string;
      mapUrl: string;
    };
    stickyBar?: { facebook?: string; zalo?: string; phone?: string };
    stats?: {
      guestCount?: number;
      attendingCount?: number;
      declinedCount?: number;
      pendingCount?: number;
    };
    createdAt: string;
  }>;
  customers: Array<{
    id: string;
    cardId: string;
    fullName: string;
    phone: string;
    email: string | null;
    relationship: string;
    invitedBy: string;
    status: "attending" | "declined" | "pending";
    notes: string;
    tags: string[];
    createdAt: string;
  }>;
};

export type SeoMeta = {
  id: string;
  entityType: string;
  entityId: string;
  title: LocalizedString;
  description: LocalizedString;
  keywords?: { vi: string[]; en: string[] };
  ogImageUrl?: string;
  robots?: string;
};

export type Company = {
  en?: Record<string, { key: string; value: string; link?: string }>;
  vi?: Record<string, { key: string; value: string; link?: string }>;
  social?: Record<string, { key: string; value: string }>;
};

export type LegalPage = {
  id: string;
  slug: string;
  label: LocalizedString;
  seo?: { title?: LocalizedString; description?: LocalizedString };
  content?: LocalizedString;
};

export type Legal = { legalPages: LegalPage[] };

function repoDataDir(): string {
  return path.join(process.cwd(), "..", "data");
}

async function readJson<T>(filename: string): Promise<T> {
  const filePath = path.join(repoDataDir(), filename);
  const raw = await readFile(filePath, "utf8");
  return JSON.parse(raw) as T;
}

export async function getCategories(): Promise<Category[]> {
  const data = await readJson<{ categories: Category[] }>("category.json");
  return data.categories;
}

export async function getPartners(): Promise<Partner[]> {
  const data = await readJson<{ partners: Partner[] }>("partner.json");
  return data.partners;
}

export async function getProducts(): Promise<Product[]> {
  const data = await readJson<{ products: Product[] }>("product.json");
  return data.products;
}

export async function getPromotions(): Promise<Promotion[]> {
  const data = await readJson<{ promotions: Promotion[] }>("promotion.json");
  return data.promotions;
}

export async function getVouchers(): Promise<Voucher[]> {
  const data = await readJson<{ vouchers: Voucher[] }>("voucher.json");
  return data.vouchers;
}

export async function getBlogs(): Promise<Blog[]> {
  const data = await readJson<{ blogs: Blog[] }>("blog.json");
  return data.blogs;
}

export async function getNews(): Promise<News[]> {
  const data = await readJson<{ news: News[] }>("news.json");
  return data.news;
}

export async function getCardCustomer(): Promise<CardCustomer> {
  return readJson<CardCustomer>("cardCustomer.json");
}

export async function getSeoMetas(): Promise<SeoMeta[]> {
  const data = await readJson<{ seoMetas: SeoMeta[] }>("metadata.json");
  return data.seoMetas;
}

export async function getCompany(): Promise<Company> {
  return readJson<Company>("company.json");
}

export async function getLegal(): Promise<Legal> {
  return readJson<Legal>("legal.json");
}

export function pickLocalized(
  value: LocalizedString | undefined,
  lang: Lang
): string {
  if (!value) return "";
  return value[lang] ?? value.vi;
}

