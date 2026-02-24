'use client';

import { cn } from '@/lib/utils';
import Link from 'next/link';
import { usePathname } from 'next/navigation';
import { ComponentProps } from 'react';

export default function NavLink({ href, ...rest }: ComponentProps<typeof Link>) {
  const pathname = usePathname();
  const isActive = pathname === href;

  return (
    <Link
      aria-current={isActive ? 'page' : undefined}
      className={cn(
        'border-b-2 border-transparent py-2 font-semibold transition-colors',
        isActive ? 'border-b-slate-900 text-slate-900' : 'text-slate-600 hover:border-b-slate-300 hover:text-slate-900'
      )}
      href={href}
      {...rest}
    />
  );
}
