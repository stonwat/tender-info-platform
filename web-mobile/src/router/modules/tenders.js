import TenderList from '../../views/tenders/TenderList.vue';
import TenderDetail from '../../views/tenders/TenderDetail.vue';

const tenderRoutes = [
  {
    path: '/',  // 实际访问路径：http://xxx.com/tender-info-platform/
    name: 'TenderList',
    component: TenderList
  },
  {
    path: '/detail',  // 实际访问路径：http://xxx.com/tender-info-platform/detail
    name: 'TenderDetail',
    component: TenderDetail,
    props: true
  }
];

export default tenderRoutes;