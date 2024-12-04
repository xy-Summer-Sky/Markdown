To set `Sidebar.vue`, `src/components/scene/Vue3RenderScene.vue`, and `src/views/Recognize.vue` as route components and enable component switching using `Sidebar.vue`, follow these steps:

1. **Update the router configuration**:
   Add routes for the components in `src/router/index.js`.

   ```javascript
   import { createRouter, createWebHistory } from 'vue-router';
   import Vue3RenderScene from '@/components/scene/Vue3RenderScene.vue';
   import Recognize from '@/views/Recognize.vue';
   
   const routes = [
     {
       path: '/three-scene',
       component: Vue3RenderScene
     },
     {
       path: '/recognize',
       component: Recognize
     },
     // Add other routes here
   ];
   
   const router = createRouter({
     history: createWebHistory(process.env.NODE_ENV === 'production' ? '/production' : ''),
     routes,
     scrollBehavior: () => ({ y: 0 }),
   });
   
   export const route404 = { path: '*', redirect: '/three-scene', hidden: true };
   
   export default router;
   ```

2. **Update `Sidebar.vue` to use the router**:
   Modify the `handleSelect` method to match the new routes.

   ```vue
   <template>
       <el-aside :width="sidebarWidth" class="sidebar" @mouseenter="expandSidebar" @mouseleave="collapseSidebar">
           <el-menu class="el-menu-vertical-demo" default-active="1" @select="handleSelect">
               <el-menu-item index="1" class="menu-item">Model Render Scene</el-menu-item>
               <el-menu-item index="2" class="menu-item">Recognize</el-menu-item>
           </el-menu>
       </el-aside>
   </template>
   
   <script>
   export default {
       data() {
           return {
               sidebarWidth: '0.1vw',
               mouseX: 0
           };
       },
       methods: {
           handleSelect(index) {
               const routes = {
                   '1': '/three-scene',
                   '2': '/recognize'
               };
               const targetRoute = routes[index];
               if (this.$route.path !== targetRoute) {
                   this.$router.push(targetRoute).then(() => this.expandSidebar());
               }
           },
           expandSidebar() {
               this.sidebarWidth = '15vw';
           },
           collapseSidebar() {
               this.sidebarWidth = '0.1vw';
           },
           handleMouseMove(event) {
               this.mouseX = event.clientX;
               if (this.mouseX <= 10) {
                   this.expandSidebar();
               } else if (this.mouseX > 10 && !document.querySelector('.sidebar:hover')) {
                   this.collapseSidebar();
               }
           }
       },
       mounted() {
           window.addEventListener('mousemove', this.handleMouseMove);
       }
   };
   </script>
   
   <style scoped>
   .sidebar {
       background-color: #f2f2f2;
       color: #333;
       position: fixed;
       top: 10px;
       left: 0;
       height: 98%;
       z-index: 1000;
       transition: width 0.3s;
   }
   .el-menu-vertical-demo {
       font-size: 30px; /* Adjust font size */
       padding: 20px 30px;  /* Adjust padding */
   }
   .menu-item {
       font-size: 30px; /* Adjust font size for menu items */
       padding: 20px 30px; /* Adjust padding for menu items */
       margin-bottom: 30px; /* Adjust spacing between menu items */
       display: flex;
       align-items: center;
       justify-content: center;
       height: 80px; /* Adjust height to match the desired click area */
       width: 100%; /* Ensure the width covers the entire sidebar */
       box-sizing: border-box; /* Include padding and border in the element's total width and height */
   }
   </style>
   ```

3. **Update the main application layout**:
   Ensure the main layout includes the `Sidebar` and a `<router-view>` to display the route components.

   ```vue
   <template>
       <div id="app">
           <Sidebar />
           <router-view />
       </div>
   </template>
   
   <script>
   import Sidebar from '@/components/Sidebar.vue';
   
   export default {
       components: {
           Sidebar
       }
   };
   </script>
   
   <style>
   #app {
       display: flex;
   }
   </style>
   ```

This setup will allow you to switch between `Vue3RenderScene.vue` and `Recognize.vue` using the `Sidebar.vue` component.