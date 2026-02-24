import React from 'react';
import { cn } from '../utils';
import { Header } from '@/components/layouts/header';
import Footer from '@/components/layouts/footer';
import RenderIf from './RenderIf';

type PageProps = {
  children: React.ReactNode;
  hideHeader?: boolean;
  hideFooter?: boolean;
  className?: string;
};

export default function Page(params: PageProps) {
  return (
    <div className={cn('flex flex-col justify-between px-4', params.className)}>
      <RenderIf condition={!params.hideHeader}>
        <Header />
      </RenderIf>
      {params.children}
      <RenderIf condition={!params.hideFooter}>
        <Footer />
      </RenderIf>
    </div>
  );
}
