import MainLayout from 'Frontend/views/MainLayout.js';
import { lazy } from 'react';
import { createBrowserRouter, RouteObject } from 'react-router-dom';
import CreditApplicationFormView from "Frontend/views/creditApplication/CreditApplicationFormView";

const AboutView = lazy(async () => import('Frontend/views/about/AboutView.js'));

export const routes  = [
  {
    element: <MainLayout />,
    handle: { title: 'Hilla CRM' },
    children: [
      { path: '/', element: <CreditApplicationFormView />, handle: { title: 'Credit Application' } },
      { path: '/about', element: <AboutView />, handle: { title: 'About' } },
    ],
  },
] as RouteObject[];

export default createBrowserRouter(routes);
